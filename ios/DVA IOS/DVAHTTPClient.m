//
//  DVAHTTPClient.m
//  DVA IOS
//
//  Created by praetorian guard on 7/26/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import "DVAHTTPClient.h"

@implementation DVAHTTPClient

- (AFHTTPRequestOperation *)HTTPRequestOperationWithRequest:(NSURLRequest *)urlRequest success:(void (^)(AFHTTPRequestOperation *, id))success failure:(void (^)(AFHTTPRequestOperation *, NSError *))failure
{
    AFHTTPRequestOperation *operation = [super HTTPRequestOperationWithRequest:urlRequest success:success failure:<#failure#>];
    
    [operation setAuthenticationAgainstProtectionSpaceBlock:^BOOL(NSURLConnection *connection,NSURLProtectionSpace *protectionSpace)
     {
         return YES;
     }];
    [operation setAuthenticationChallengeBlock:^(NSURLConnection *connection,NSURLAuthenticationChallenge *challenge)
     {
         if([challenge.protectionSpace.authenticationMethod isEqualToString:NSURLAuthenticationMethodServerTrust])
         {
             [challenge.sender useCredential:[NSURLCredential credentialForTrust:(__bridge SecTrustRef)(challenge.protectionSpace)] forAuthenticationChallenge:challenge];
         }
     }];
    return operation;
}

@end
