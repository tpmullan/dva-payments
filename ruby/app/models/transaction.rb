class Transaction < ActiveRecord::Base
  attr_accessible :amount, :balance, :fromAccount, :memo, :toAccount, :user_id, :account_id
  belongs_to :account
  validates_presence_of :amount
  validates_presence_of :balance
  validates_presence_of :fromAccount
  validates_presence_of :toAccount
  validates_presence_of :user_id
  validates_presence_of :account_id
  validates_associated :account
  default_scope order: 'transactions.created_at DESC'

end
