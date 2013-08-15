class AddIndexToQRtokenToken < ActiveRecord::Migration
  def change
	  add_index :q_rtokens, :token, unique: true
  end
end
