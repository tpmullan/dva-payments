//
//  MoveMoneyControllerViewController.h
//  DVA IOS
//
//  Created by praetorian guard on 7/29/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MoveMoneyRequest.h"
#import "ContactStorage.h"
#import <ZBarSDK/ZBarSDK.h>

@interface MoveMoneyControllerViewController : UIViewController
<MoveMoneyRequestDelegate,UIPickerViewDelegate,ZBarReaderDelegate>
{
    
}
@property (weak, nonatomic) IBOutlet UISlider *transferModeSlider;
@property (weak, nonatomic) IBOutlet UITextField *transferToAccount;
@property (weak, nonatomic) IBOutlet UITextField *transferFromAccount;
@property (weak, nonatomic) IBOutlet UITextField *transferAmount;
@property (weak, nonatomic) IBOutlet UIButton *makeTransfer;
@property (weak, nonatomic) IBOutlet UILabel *transferModeMessage;
@property (strong, nonatomic) NSMutableArray *accountPeopleList;
- (IBAction)backgroundClick:(id)sender;


@end
