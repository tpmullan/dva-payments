class QRtoken < ActiveRecord::Base
  attr_accessible :account_number, :amount
  belongs_to :account

  before_save :create_token

  validates_presence_of :account_number, :on => :create
  validates_presence_of :amount, :on => :create

  private

  def create_token
    self.token = SecureRandom.urlsafe_base64
  end
end
