class CreateAccounts < ActiveRecord::Migration
  def change
    create_table :accounts do |t|
      t.string :number
      t.float :balance
      t.integer :user_id

      t.timestamps
    end
    add_index :accounts, [:user_id, :number]
  end
end
