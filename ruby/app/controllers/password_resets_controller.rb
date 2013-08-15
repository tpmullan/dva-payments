class PasswordResetsController < ApplicationController
	
  def create
    user = User.find_by_email(params[:email])
    user.send_password_reset if user
    redirect_to root_url, :notice => "Email sent with password reset instructions."
  end

  def new
  end

  def edit
    @user = User.find_by_remember_token!(params[:id])
  end

  def update
    @user = User.find_by_remember_token!(params[:id])
    @user.updating_password = true
    if @user.update_attributes(params[:user])
       redirect_to root_url, :notice => "Password has been reset!"
    else
       render :edit
    end
  end
end
