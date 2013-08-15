//
//  MyFriendsController.m
//  DVA IOS
//
//  Created by Praetorian Guard on 8/8/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import "MyFriendsController.h"
#import "MyFriendsRequest.h"
#import "ContactStorage.h"

@interface MyFriendsController ()

@end

@implementation MyFriendsController

@synthesize UpdateFriends;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)friendsUpdate:(id)sender
{
    MyFriendsRequest * client = [[MyFriendsRequest alloc] init];
    [client sendFriends];
}

@end
