# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended to check this file into your version control system.

ActiveRecord::Schema.define(:version => 20130715214726) do

  create_table "accounts", :force => true do |t|
    t.string "number"
    t.float "balance"
    t.integer "user_id"
    t.datetime "created_at", :null => false
    t.datetime "updated_at", :null => false
    t.string "name"
  end

  add_index "accounts", ["user_id", "number"], :name => "index_accounts_on_user_id_and_number"

  create_table "friends", :force => true do |t|
    t.integer "user_id"
    t.integer "friend_user_id"
    t.datetime "created_at", :null => false
    t.datetime "updated_at", :null => false
  end

  create_table "q_rtokens", :force => true do |t|
    t.string "token", :null => false
    t.float "amount", :null => false
    t.string "account_number", :null => false
    t.datetime "created_at", :null => false
    t.datetime "updated_at", :null => false
  end

  add_index "q_rtokens", ["token"], :name => "index_q_rtokens_on_token", :unique => true

  create_table "transactions", :force => true do |t|
    t.string "toAccount"
    t.string "fromAccount"
    t.float "amount"
    t.float "balance"
    t.string "memo"
    t.integer "user_id"
    t.datetime "created_at", :null => false
    t.datetime "updated_at", :null => false
    t.string "account_id"
  end

  add_index "transactions", ["user_id", "created_at"], :name => "index_transactions_on_user_id_and_created_at"

  create_table "users", :force => true do |t|
    t.string "name"
    t.string "email"
    t.datetime "created_at", :null => false
    t.datetime "updated_at", :null => false
    t.string "password_digest"
    t.string "remember_token"
    t.string "username"
    t.boolean "admin", :default => false
    t.string "reset_token"
    t.datetime "reset_sent_at"
  end

  add_index "users", ["email"], :name => "index_users_on_email", :unique => true
  add_index "users", ["remember_token"], :name => "index_users_on_remember_token"
  add_index "users", ["username"], :name => "index_users_on_username", :unique => true

end
