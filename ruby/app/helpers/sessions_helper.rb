module SessionsHelper
  def sign_in (user)
    cookies.permanent[:remember_token] = user.remember_token
    #session[:current_user_id] = user.id
    self.current_user = user
  end
  
  def signed_in?
    !current_user.nil?
  end

  def signed_in_user
    unless signed_in?
      respond_to do |format|
        format.html { redirect_to signin_url, notice: "Please sign in." }
        format.json { render json: {:status => false, :message => ["please sign in."], :userId=>0}, status: :unauthorized }
      end
    end
  end
  def current_user=(user)
    @current_user = user
  end

  def current_user
    @current_user ||= User.find_by_remember_token(cookies[:remember_token])
  end
  
  def sign_out
    self.current_user = nil
    #session[:current_user_id] = nil
    cookies.delete(:remember_token)
  end

  def admin_user
    unless current_user.admin?
      respond_to do |format|
        format.html { redirect_to root_path, notice: "Please sign in." }
        format.json { render json: {:status => false, :message => ["You are not an Admin"], :userId=>0}, status: :unauthorized }
      end
    end
  end

  def admin?
	  current_user.admin?
  end
end
