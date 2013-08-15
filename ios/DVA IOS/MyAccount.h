//
//  MyAccount.h
//  DVA IOS
//
//  Created by praetorian guard on 7/28/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Account.h"

@class MyAccount;
@protocol MyAccountDelegate<NSObject>
@optional
-(void) finishWithStatus:(BOOL) status withMessage:(NSString*) message;
-(void) finishWithAccount:(NSMutableArray *) response;
-(void) finishWithBalance: (NSMutableArray *) response;
-(void) finishWithUserProfile:(NSArray *) response;
-(void) finishWithStatusAndExit:(BOOL) status;
@end

@interface MyAccount : NSObject
{
    
    id<MyAccountDelegate> delegate;
    
}
@property(nonatomic, retain) id<MyAccountDelegate> delegate;

- (void)  getUser:(NSString *) userId;
- (void)  updateUserwithEmail: (NSString *) email withUsername: (NSString *) username withPassword:(NSString *) password;
- (void) deleteUser;
- (void) getAccounts;
- (void) getBalance;


@end