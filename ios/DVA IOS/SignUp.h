//
//  SignUp.h
//  DVA IOS
//
//  Created by Tom M. on 8/14/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <AFNetworking/AFNetworking.h>
#import "AccountStorage.h"

@class SignUp;
@protocol SignUpDelegate<NSObject>
-(void) finishwithStatus:(BOOL) status withMessage:(NSString*) message;
@end

@interface SignUp : NSObject
{
    
    id<SignUpDelegate> delegate;
    
}

@property(nonatomic, retain) id<SignUpDelegate> delegate;

- (void) signUpUser:(NSString *)username withName:(NSString *)name withPassword:(NSString *)password withConfirmation:(NSString *)confirmation withEmail:(NSString *)email;


@end