//
//  RoundedEdges.m
//  DVA IOS
//
//  Created by Tom M. on 8/9/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import "RoundedEdges.h"

@implementation RoundedEdges

- (void)drawRect:(CGRect)rect
{
    self.layer.masksToBounds = YES;
    self.layer.cornerRadius=10.0;
}

@end
