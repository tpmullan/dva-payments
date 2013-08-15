PaymentServer::Application.routes.draw do

  get "friends/show"

  get "friends/create"

  resources :password_resets, only: [:new, :create, :update, :edit]
  resources :users
  resources :sessions, only: [:new, :create, :destroy]

  #User
  root to: 'transaction#balance'
  match '/signup', to: 'users#new'
  match '/signin', to: 'sessions#new'
  match '/signout', to: 'sessions#destroy', via: :delete
  match '/users/valid', to: 'users#valid', via: :post

  #Banking
  match '/transfer', to: 'transaction#create', via: :post
  match '/transfer', to: 'transaction#new', via: :get
  match '/transfer/new', to: 'transaction#form', via: :get
  match '/transactions(/:id)', to: 'transaction#transactions', :as => :transactions
  # match '/transactions', to: 'transaction#index', via: :get
  match '/balance', to: 'transaction#balance', via: :get

  #Token
  match '/token', to: 'transaction#token', via: :get
  match '/token', to: 'transaction#gen_token', via: :post
  match '/token', to: 'transaction#put_token', via: :put

  #Friends
  match '/friends', to: 'friends#show', via: :get
  match '/friends', to: 'friends#create', via: :post


  #Common redirects
  get '/sessions', to: redirect('/')
  get '/signout', to: redirect('/signin')

  # The priority is based upon order of creation:
  # first created -> highest priority.

  # Sample of regular route:
  #   match 'products/:id' => 'catalog#view'
  # Keep in mind you can assign values other than :controller and :action

  # Sample of named route:
  #   match 'products/:id/purchase' => 'catalog#purchase', :as => :purchase
  # This route can be invoked with purchase_url(:id => product.id)

  # Sample resource route (maps HTTP verbs to controller actions automatically):
  #   resources :products

  # Sample resource route with options:
  #   resources :products do
  #     member do
  #       get 'short'
  #       post 'toggle'
  #     end
  #
  #     collection do
  #       get 'sold'
  #     end
  #   end

  # Sample resource route with sub-resources:
  #   resources :products do
  #     resources :comments, :sales
  #     resource :seller
  #   end

  # Sample resource route with more complex sub-resources
  #   resources :products do
  #     resources :comments
  #     resources :sales do
  #       get 'recent', :on => :collection
  #     end
  #   end

  # Sample resource route within a namespace:
  #   namespace :admin do
  #     # Directs /admin/products/* to Admin::ProductsController
  #     # (app/controllers/admin/products_controller.rb)
  #     resources :products
  #   end

  # You can have the root of your site routed with "root"
  # just remember to delete public/index.html.
  # root :to => 'welcome#index'

  # See how all your routes lay out with "rake routes"

  # This is a legacy wild controller route that's not recommended for RESTful applications.
  # Note: This route will make all actions in every controller accessible via GET requests.
  # match ':controller(/:action(/:id))(.:format)'
end
