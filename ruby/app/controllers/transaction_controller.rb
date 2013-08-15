class TransactionController < ApplicationController
  before_filter :signed_in_user

  # GET /transfer
  def new
    @transaction = Transaction.new
    @user = current_user
    @accounts = Account.find_all_by_user_id(@user.id).collect {
        |a| ["#{a.name.capitalize} Account (...#{a.number.match(/(.{0,#{4}}$)/)}):  $#{a.balance}", a.number]
    }
  end

  # GET /transfer/new
  def form
    @transaction = Transaction.new
    @user = current_user
    @accounts = Account.find_all_by_user_id(@user.id).collect {
        |a| ["#{a.name.capitalize} Account (...#{a.number.match(/(.{0,#{4}}$)/)}):  $#{a.balance}", a.number]
    }
  end

  # POST /transfer
  # POST /transfer.json
  def create
    @transaction = Transaction.new(params[:transfer])
    @user = current_user
    @accounts = Account.find_all_by_user_id(@user.id).collect {
        |a| ["#{a.name.capitalize} Account (...#{a.number.match(/(.{0,#{4}}$)/)}):  $#{a.balance}", a.number]
    }

    #Autoset fromAccount if not present
    @transaction.fromAccount ||= @user.email

    #set accounts
    @account = Account.find_by_number(@transaction.fromAccount)
    @account ||= Account.find_by_user_id(User.find_by_email(@transaction.fromAccount))
    toAccount = Account.find_by_number(@transaction.toAccount)
    toAccount ||= Account.find_by_user_id(User.find_by_email(@transaction.toAccount))

    respond_to do |format|
      if toAccount && @account
        if toAccount.id != @account.id && @transaction.amount > 0 && @account.user_id == @user.id
          #set up transactions
          @transaction.user_id=@user.id
          @transaction.account_id = @account.id
          otherTransaction = Transaction.new(:amount => @transaction.amount, :toAccount => @transaction.toAccount,
                                             :fromAccount => @transaction.fromAccount, :user_id => toAccount.user_id,
                                             :account_id => toAccount.id, :memo => @transaction.memo)
          @transaction.amount = -@transaction.amount
          #update the account balance
          @transaction.balance = @account.balance + @transaction.amount
          otherTransaction.balance = toAccount.balance + otherTransaction.amount

          #update Friends
          new_friend = Friends.new(:user_id => @user.id, :friend_user_id => toAccount.user_id)
          new_friend.save

          if  @transaction.save
            otherTransaction.save
            if @account.update_attributes({:balance => @transaction.balance})
              if  toAccount.update_attributes({:balance => otherTransaction.balance})
                format.html { redirect_to transactions_path, notice: 'Transaction was successfully created.' }
                format.json { render json: {:status => true, :message => ["Transaction successful"],
                                            :userId => current_user.id}, status: :ok }
              else
                format.html { render action: "new", notice: "Something went wrong" }
                format.json { render json: {:status => false, :message => toAccount.errors.full_messages, :userId => 0},
                                     status: :unprocessable_entity }
              end
            else
              format.html { render action: "new", notice: "Something went wrong" }
              format.json { render json: {:status => false, :message => @account.errors.full_messages, :userId => 0},
                                   status: :unprocessable_entity }
            end
          else
            format.html { render action: "new", notice: "Something went wrong" }
            format.json { render json: {:status => false, :message => @transaction.errors.full_messages, :userId => 0},
                                 status: :unprocessable_entity }
          end
        else
          format.html { render action: "new", notice: "The information supplied is not correct" }
          format.json { render json: {:status => false, :message => ["The information supplied is not correct"],
                                      :userId => 0}, status: :unprocessable_entity }
        end
      else
        format.html { render action: "new", notice: "The information supplied is not correct" }
        format.json { render json: {:status => false, :message => ["The account information supplied is not correct"],
                                    :userId => 0}, status: :unprocessable_entity }
      end
    end
  end

  #/transactions
  #/transactions.json
  def transactions
    @user = current_user
    if params[:id]
      @account = Account.find(params[:id])
      if @account.user_id != @user.id
        @account = nil
      end
    else
      @account = Account.find_by_user_id(@user.id)
    end

    respond_to do |format|
      if @account
        @transactions = Transaction.find_all_by_account_id(@account.id)

        format.html { render action: 'index' } # index.html.erb
        format.json { render json: {:transactions => @transactions}, status: :ok }
      else
        format.html { redirect_to root_path, notice: 'Error loading that account' }
        format.json { render json: {:transactions => ['Error loading that account']}, status: :ok }
      end

    end
  end

  #/balance
  #/balance.json
  def balance
    @user = current_user
    @accounts = Account.find_all_by_user_id(@user.id)
    respond_to do |format|
      format.html # balance.html.erb
      format.json { render json: {:accounts => @accounts.collect { |account| {:name => account.name,
                                                                              :number => account.number,
                                                                              :balance => account.balance,
                                                                              :id => account.id} }}, status: :ok }
    end
  end

  # GET /token
  # GET /token.json
  def token
    @token = QRtoken.new
    @user = current_user
    @accounts = Account.find_all_by_user_id(@user.id).collect {
        |a| ["#{a.name.capitalize} Account (...#{a.number.match(/(.{0,#{4}}$)/)}):  $#{a.balance}", a.number]
    }
  end

  # POST /token
  # POST /token.json
  def gen_token
    @user = current_user
    if params[:token][:account_number].blank?
      params[:token].delete("account_number")
    end
    @token = QRtoken.new(params[:token])
    @token.account_number ||= Account.select(:number).where(["user_id = ?", @user.id]).first.number
    @account = Account.find_by_number(@token.account_number)
    respond_to do |format|
      if  @account && @token.amount > 0
        if @account.user_id == @user.id

          if @token.save
            @qr = RQRCode::QRCode.new(@token.token)
            format.html { render action: "qrcode", notice: 'Transaction was successfully created.' }
            format.json { render json: {:status => true, :token => @token.token} }
          else
            format.html { redirect_to token_path, notice: 'The token could not be made' }
            format.json { render json: {:status => false, :token => ""}, status: :unprocessable_entity }
          end
        else
          format.html { redirect_to token_path, notice: 'The token could not be made' }
          format.json { render json: {:status => false, :token => ""}, status: :unprocessable_entity }
        end
      else
        format.html { redirect_to token_path, notice: 'The token could not be made' }
        format.json { render json: {:status => false, :token => ""}, status: :unprocessable_entity }
      end
    end
  end

  # PUT /token
  # PUT /token.json
  def put_token
    @user = current_user
    @token = QRtoken.find_by_token(params[:token])

    respond_to do |format|
      if @token
        @account = Account.find_by_number(@token.account_number)
        toAccount = Account.find_by_number(params[:account])

        if toAccount && @account
          if toAccount.id != @account.id && @token.amount > 0
            #set up transactions
            @transaction = Transaction.new(:amount => -@token.amount, :toAccount => toAccount.number,
                                           :fromAccount => @token.account_number, :user_id => @account.user_id,
                                           :account_id => @account.id, :memo => "token transfer")

            otherTransaction = Transaction.new(:amount => @token.amount, :toAccount => toAccount.number,
                                               :fromAccount => @transaction.fromAccount, :user_id => toAccount.user_id,
                                               :account_id => toAccount.id, :memo => "token transfer")

            #update the account balance
            @transaction.balance = @account.balance + @transaction.amount
            otherTransaction.balance = toAccount.balance + otherTransaction.amount

            #update Friends
            new_friend = Friends.new(:user_id => @user.id, :friend_user_id => toAccount.user_id)
            new_friend.save

            if  @transaction.save
              otherTransaction.save
              if @account.update_attributes({:balance => @transaction.balance})
                if  toAccount.update_attributes({:balance => otherTransaction.balance})
                  @token.destroy
                  format.html { redirect_to transactions_path, notice: 'Transaction was successfully created.' }
                  format.json { render json: {:status => true, :message => ["Transaction successful"]} }
                else
                  format.html { render action: "new", notice: "Something went wrong" }
                  format.json { render json: {:status => false, :message => toAccount.errors.full_messages}, status: :unprocessable_entity }
                end
              else
                format.html { render action: "new", notice: "Something went wrong" }
                format.json { render json: {:status => false, :message => @account.errors.full_messages}, status: :unprocessable_entity }
              end
            else
              format.html { render action: "new", notice: "Something went wrong" }
              format.json { render json: {:status => false, :message => @transaction.errors.full_messages}, status: :unprocessable_entity }
            end
          else
            format.html { render action: "new", notice: "The information supplied is not correct" }
            format.json { render json: {:status => false, :message => ["The information supplied is not correct"]}, status: :unprocessable_entity }
          end
        else
          format.html { render action: "new", notice: "The information supplied is not correct" }
          format.json { render json: {:status => false, :message => ["The information supplied is not correct"]}, status: :unprocessable_entity }
        end
      else
        format.html { render action: "new", notice: "The information supplied is not correct" }
        format.json { render json: {:status => false, :message => ["The information supplied is not correct"]}, status: :unprocessable_entity }
      end
    end
  end
end
