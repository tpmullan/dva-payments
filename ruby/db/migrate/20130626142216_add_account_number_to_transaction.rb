class AddAccountNumberToTransaction < ActiveRecord::Migration
  def change
    add_column :transactions, :account_id, :string
  end
end
