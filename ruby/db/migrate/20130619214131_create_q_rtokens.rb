class CreateQRtokens < ActiveRecord::Migration
  def change
    create_table :q_rtokens do |t|
      t.string :token, :null => false
      t.float :amount, :null => false
      t.string :account_number, :null => false

      t.timestamps
    end
  end
end
