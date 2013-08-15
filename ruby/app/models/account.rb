class Account < ActiveRecord::Base
  attr_accessible :balance, :number, :user_id, :name
  belongs_to :user

  before_create :create_account_number

  validates_presence_of :balance
  validates_presence_of :user_id
  validates_presence_of :name
  validates_associated :user
  has_many :transactions, dependent: :destroy
  private

  def create_account_number
    self.number = SecureRandom.urlsafe_base64
  end

end
