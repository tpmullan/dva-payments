class CreateTransactions < ActiveRecord::Migration
  def change
    create_table :transactions do |t|
      t.string :toAccount
      t.string :fromAccount
      t.float :amount
      t.float :balance
      t.string :memo
      t.integer :user_id

      t.timestamps
    end
    add_index :transactions, [:user_id, :created_at]
  end
end
