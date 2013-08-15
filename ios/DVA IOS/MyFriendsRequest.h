//
//  MyFriendsRequest.h
//  DVA IOS
//
//  Created by Praetorian Guard on 8/8/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <AddressBook/AddressBook.h>
#import "ContactStorage.h"

@class MyFriendsRequest;

@protocol MyFriendsRequestDelegate<NSObject>
@optional
-(void) finishWithStatus:(BOOL) status withMessage:(NSString *) message;

@end
@interface MyFriendsRequest : NSObject
{
    id<MyFriendsRequestDelegate> delegate;
}

@property(nonatomic,retain) id<MyFriendsRequestDelegate> delegate;


-(void) sendFriends;

@end
