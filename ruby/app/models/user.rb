class User < ActiveRecord::Base
  attr_accessible :email, :username, :name, :password, :password_confirmation, :admin
  has_secure_password
  has_many :accounts, dependent: :destroy
  has_many :transactions, dependent: :destroy

  before_save { |user| user.email = email.downcase }
  before_save :create_remember_token

  validates_presence_of :email, :on => :create
  validates_presence_of :username, :on => :create
  validates_presence_of :name, :on => :create

  validates :name, length: {maximum: 50}, :if => Proc.new { !:name.nil? }
  validates :username, length: {maximum: 50}, uniqueness: true, :if => Proc.new { !:username.nil? }
  VALID_EMAIL_REGEX = /\A[\w+\-.]+@[a-z\d\-.]+\.[a-z]+\z/i
  validates :email, format: {with: VALID_EMAIL_REGEX},
            uniqueness: {case_sensitive: false},
            :if => Proc.new { !:email.nil? }
  validates :password, length: {minimum: 6}, :if => :should_validate_password?
  validates :password_confirmation, presence: true, :if => :should_validate_password?

  attr_accessor :updating_password

  def send_password_reset
    create_remember_token
    save!
    UserMailer.password_reset(self).deliver
  end

  private

  def create_remember_token
    self.remember_token = SecureRandom.urlsafe_base64
  end

  def should_validate_password?
    updating_password || new_record?
  end
end
