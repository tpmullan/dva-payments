//
//  Contact.m
//  DVA IOS
//
//  Created by Praetorian Guard on 8/9/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import "Contact.h"

@implementation Contact

@synthesize name;
@synthesize username;
@synthesize email;

-(void) setContactName:(NSString *)_name withUsername:(NSString *)_username withEmail:(NSString *)_email
{
    name = _name;
    username = _username;
    email = _email;
}

-(NSString *) description
{
    return [NSString stringWithFormat:@"%@ (%@)",name,email];
}


@end
