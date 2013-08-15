//
//  LoginRequest.h
//  DVA IOS
//
//  Created by praetorian guard on 7/25/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import <Foundation/Foundation.h>
@class LoginRequest;
@protocol LoginRequestDelegate<NSObject>
-(void) finishwithStatus:(BOOL) status withMessage:(NSString*) message;
@end

@interface LoginRequest : NSObject
{

    id<LoginRequestDelegate> delegate;
    
}
@property(nonatomic, retain) id<LoginRequestDelegate> delegate;

- (void) authenticateUser:(NSString *)username withPassword:(NSString *)password;


@end
