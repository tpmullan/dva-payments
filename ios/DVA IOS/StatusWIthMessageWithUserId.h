//
//  StatusWIthMessageWithUserId.h
//  DVA IOS
//
//  Created by praetorian guard on 7/26/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface StatusWIthMessageWithUserId : NSObject
    @property bool status;
    @property NSString * message;
    @property int userId;


- (id) init;

@end
