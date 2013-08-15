class FriendsController < ApplicationController
  before_filter :signed_in_user

  def show
    @user = current_user
    friend_ids = Friends.find_all_by_user_id(@user)
    @friends = Array.new
    friend_ids.each do |f|
      user = User.find(f.friend_user_id)
      @friends.push({:username => user.username, :email => user.email, :name => user.name})
    end
    respond_to do |format|
      format.html # show.html.erb
      format.json { render json: @friends, status: :ok }
    end
  end

  def create
    @user = current_user
    emails = params["emails"]
    if emails && @user
      emails.each do |email|
        friend = User.find_by_email(email)
        if friend
          new_friend = Friends.new(:user_id => @user.id, :friend_user_id => friend.id)
          new_friend.save
        end
      end
      respond_to do |format|
        format.html { render action: "show", :notice => "My Friends updated!" }
        format.json { render json: {:status => true, :message => ['My Friends updated!'], :userId => @user.id} }
      end
    else
      respond_to do |format|
        format.html { render action: "show", :notice => "No emails where received" }
        format.json { render json: {:status => false, :message => ['No emails where received'], :userId => @user.id} }
      end
    end
  end
end