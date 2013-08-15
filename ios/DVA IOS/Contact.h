//
//  Contact.h
//  DVA IOS
//
//  Created by Praetorian Guard on 8/9/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Contact : NSObject

@property (nonatomic, strong) NSString * name;
@property (nonatomic, strong) NSString * username;
@property (nonatomic, strong) NSString * email;

-(void) setContactName:(NSString *) name withUsername:(NSString *) username withEmail:(NSString *) email;

@end
