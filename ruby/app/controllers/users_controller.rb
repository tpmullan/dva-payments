class UsersController < ApplicationController
  before_filter :signed_in_user, only: [:index, :edit, :update, :destroy, :show]
  before_filter :admin_user, only: :index
  # GET /users
  # GET /users.json
  def index
    @users = User.all

    respond_to do |format|
      format.html # index.html.erb
      format.json {
        render json: @users.collect {
            |a| {:name => a.name, :email => a.email, :username => a.username}
        }, status: :ok
      }
    end
  end

  # GET /users/1
  # GET /users/1.json
  def show
    @user = User.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.json { render json: @user, status: :ok }
    end
  end

  # GET /users/new
  # GET /users/new.json
  def new
    @user = User.new

    respond_to do |format|
      format.html # new.html.erb
      format.json { render json: @user, status: :ok }
    end
  end

  # GET /users/1/edit
  def edit
    @user = User.find(params[:id])
  end

  # POST /users
  # POST /users.json
  def create
    @user = User.new(params[:user])

    respond_to do |format|
      if @user.save
        account =Account.new(:balance => 500, :user_id => @user.id, :name => 'CHECKING')
        account.save
        transaction = Transaction.new(:amount => 500, :balance => 500, :toAccount => account.number,
                                      :fromAccount => 'admin@apps.loginto.me', :user_id => @user.id,
                                      :account_id => account.id, :memo => "Don't spend it all in one place")
        transaction.save
        account2 =Account.new(:balance => 2500, :user_id => @user.id, :name => 'SAVINGS')
        account2.save
        transaction2 = Transaction.new(:amount => 2500, :balance => 2500, :toAccount => account2.number,
                                       :fromAccount => 'admin@apps.loginto.me', :user_id => @user.id,
                                       :account_id => account2.id, :memo => "Don't spend it all in one place")
        transaction2.save
        sign_in @user
        format.html { redirect_to balance_path, notice: 'User was successfully created.' }
        format.json { render json: {:status => true, :message => ["Welcome #{@user.name}"], :userId => @user.id}, status: :ok }
      else
        format.html { render action: "new" }
        format.json { render json: {:status => false, :message => @user.errors.full_messages, :userId => 0}, status: :unprocessable_entity }
      end
    end
  end

  # PUT /users/1
  # PUT /users/1.json
  def update
    @user = User.find(params[:id])

    if params[:user].nil?
      respond_to do |format|
        format.html { render action: "edit" }
        format.json { render json: {:status => false, :message => "You didn't change anything", :userId => @user.id}, status: :unprocessable_entity }
      end

    else
      # required for settings form to submit when password is left blank
      if params[:user][:password].blank?
        params[:user].delete("password")
        params[:user].delete("password_confirmation")
      elsif  params[:user][:password]
        @user.updating_password = true
      end

      respond_to do |format|
        if @user.update_attributes(params[:user])
          format.html { redirect_to @user, notice: 'User was successfully updated.' }
          format.json { render json: {:status => true, :message => ["Thanks for the update #{@user.name}"], :userId => @user.id}, status: :ok }
        else
          format.html { render action: "edit" }
          format.json { render json: {:status => false, :message => @user.errors.full_messages, :userId => @user.id}, status: :unprocessable_entity }
        end
      end
    end
  end

  # DELETE /users/1
  # DELETE /users/1.json
  def destroy
    @user = User.find(params[:id])
    @user.destroy

    respond_to do |format|
      format.html { redirect_to users_url, notice: "You will be missed #{@user.name}" }
      format.json { render json: {:status => true, :message => ["You will be missed #{@user.name}"], :userId => @user.id}, status: :ok }
    end
  end

  # POST /users/valid
  # POST /users/valid.json
  def valid
    check = User.find_by_email(params[:email])
    if check
      respond_to do |format|
        format.html { redirect_to root_path }
        format.json { render json: {:status => true, :message => ["#{check.username}"]} }
      end
    else
      respond_to do |format|
        format.html { redirect_to root_path }
        format.json { render json: {:status => false, :message => ["The user does not exist"]} }
      end
    end
  end
end
