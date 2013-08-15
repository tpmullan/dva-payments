dva-payments
============

DVA Payments - A vulnerable way to hold your money

DVA Setup Guide:

We have created a full server-client implementation for a mobile payment processor. This guide will step thru much of the environment setup for each of the IOS/Android/Ruby.

Ruby Server:
------------  
###Just Launch:
1) Start the Amazon AMI (463992082663/DVA-Server-1.0)
2) Similar process as this guide (http://docs.openvpn.net/how-to-tutorialsguides/virtual-platforms/amazon-ec2-appliance-ami-quick-start-guide/)
*) Ensure network security groups allow access to port 443 from your IP. 

-OR-  
###Compile and Setup:
1) Install ruby on rails (https://www.digitalocean.com/community/articles/how-to-install-ruby-on-rails-on-ubuntu-12-04-lts-precise-pangolin-with-rvm)
2) Clone our git repo from github.
```bash
$ git clone  https://github.com/gitpraetorianlabs/dva-payments.git
```
3) run “bundle install; rails s” in the ruby directory

Android:
--------
###Just Download:
1) Download from the Play Store (https://play.google.com/store/apps/details?id=com.praetorian.dva)

-OR-  

###Compile and Setup:
1) Setup the Android Development Environment.  (http://developer.android.com/sdk/index.html)
2) Clone our git repo from github.
```bash
$ git clone  https://github.com/gitpraetorianlabs/dva-payments.git
```
3) Open the project inside of eclipse
	file > import
	Android Project > $path_to_git/android/
*) If you have any issues ensure you have the API 17 (Android 4.2) files. You can download them using the sdk manager under Window>SDK Manager in eclipse. 

4) Compile and Run. 

5) You will need a server running in-order to run the application. There is a preconfigured AMI Image on Amazon Web Services which will get you up and running quickly. Under the Proxy Settings Tab in the application you will need to set the ‘Server Host’ to point to your server instance. In most cases it will be an ip address of your computer which is running the ruby server or the AWS Image which is running the server. 


IOS:
----
###Just Download:
1) Download from App Store (Link TBD)

-OR-  
###Compile and Setup:
1) Download and setup XCode (https://developer.apple.com/xcode/)
2) Clone our git repo from github
```bash
$ git clone https://github.com/gitpraetorianlabs/dva-payments.git
```
3) Open the xcode workspace file inside xcode or by double click on it in finder
	$path_to_git/ios/DVA IOS.xcworkspace
4) Click the Run Button (Ensure you have IOS Simulator select).
*) You must have Apple Computer for development on IOS. If you do not have an apple computer you can view the code but can’t make modifications


