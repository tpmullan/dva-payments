class SessionsController < ApplicationController
  
  def new
  end


  # POST /sessions
  # POST /sessions.json
  def create
    @user = User.find_by_email(params[:session][:email].downcase)
    @user ||= User.find_by_username(params[:session][:email].downcase)
    respond_to do |format|
      if @user && @user.authenticate(params[:session][:password])
        sign_in @user
        #redirect_to user
        format.html { redirect_to balance_path, notice: "Welcome #{@user.name}."}
        format.json { render json: {:status => true, :message => ["Welcome #{@user.name}"], :userId=>@user.id}}
      else
        format.html { render action: "new", :notice => "Invalid email/password combination"}
        format.json { render json: {:status => false, :message => ['Invalid email/password combination'], :userId=>0}}
      end
    end
  end

  def destroy
    sign_out
    respond_to do |format|
      format.html { redirect_to root_url, :notice => "Thank you for visiting" }
      format.json { render json: {:status => true, :message => ['Thank you for visiting'], :userId=>0}}
    end
  end

end
