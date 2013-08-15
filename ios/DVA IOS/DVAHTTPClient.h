//
//  DVAHTTPClient.h
//  DVA IOS
//
//  Created by praetorian guard on 7/26/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import "AFHTTPClient.h"

@interface DVAHTTPClient : AFHTTPClient

- (AFHTTPRequestOperation *)HTTPRequestOperationWithRequest:(NSURLRequest *)urlRequest success:(void (^)(AFHTTPRequestOperation *, id))success failure:(void (^)(AFHTTPRequestOperation *, NSError *))failure;

@end
