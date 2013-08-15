class Friends < ActiveRecord::Base
  attr_accessible :friend_user_id, :user_id
  validates_presence_of :friend_user_id
  validates_presence_of :user_id

  validates :friend_user_id, uniqueness: {scope: :user_id,
                                          message: "You only need to be friends once!"}
end
