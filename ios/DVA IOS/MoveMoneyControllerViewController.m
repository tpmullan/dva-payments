//
//  MoveMoneyControllerViewController.m
//  DVA IOS
//
//  Created by praetorian guard on 7/29/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import "MoveMoneyControllerViewController.h"
#import "AccountStorage.h"
#import "Account.h"
#import <iOS-QR-Code-Encoder/QRCodeGenerator.h>

@interface MoveMoneyControllerViewController ()

@end

@implementation MoveMoneyControllerViewController

@synthesize transferAmount;
@synthesize transferFromAccount;
@synthesize transferModeSlider;
@synthesize transferToAccount;
@synthesize makeTransfer;
@synthesize transferModeMessage;
@synthesize accountPeopleList;
int popupMode;
UIButton *imageButton;
typedef enum
{
    contactbookmode,accountbookmode
} popupModeEnum;


typedef enum
{
    simple,receive,send
} sliderMode;

- (IBAction)backgroundClick:(id)sender {
    [transferAmount resignFirstResponder];
    [transferFromAccount resignFirstResponder];
    [transferModeMessage resignFirstResponder];
    [transferToAccount resignFirstResponder];
    [makeTransfer resignFirstResponder];
    [transferToAccount resignFirstResponder];
}
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void) generateQRCodeWithToken: (NSString *) token
{
    
   UIImage* image = [QRCodeGenerator qrImageForString:token imageSize:self.view.bounds.size.width];
    UIColor * color = [UIColor colorWithRed:255/255.0f green:255/255.0f blue:255/255.0f alpha:1.0f];
    
    
    
    
    
    
	UIImageView* imageView = [[UIImageView alloc] initWithImage:image];
    [imageView setBackgroundColor:color];
    CGFloat qrSize = self.view.bounds.size.width - 5 * 2;
	imageView.frame = CGRectMake(5, (self.view.bounds.size.height - qrSize) / 2,
                                 qrSize, qrSize);
    imageButton = [UIButton buttonWithType:UIButtonTypeCustom];
    imageButton.frame = CGRectMake(0,(self.view.bounds.size.height-self.view.bounds.size.width)/2, self.view.bounds.size.width, self.view.bounds.size.width);
    [imageButton setImage:image forState:UIControlStateNormal];
    
    [imageButton setBackgroundColor:color];
    
    [imageButton addTarget:self action:@selector(buttonPushed:)
          forControlEvents:UIControlEventTouchUpInside];
    
    
    
    
//	[imageView layer].magnificationFilter = kCAFilterNearest;
    
	[self.view addSubview:imageButton];
  // [imageView release];
}

- (void) buttonPushed:(id)sender
{
	[imageButton removeFromSuperview];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	NSLog(@"Populting Data from Account Storage");
    
    
    accountPeopleList = [[NSMutableArray alloc] init];
    
    [transferModeSlider setValue:0];
    [self updateSliderWithSender: transferModeSlider];
    
    Account * singleAccount = [AccountStorage getAccountWithIndex:0];
    
    
    //add accounts into our global list for the pb book
    for(int k =0; k < [AccountStorage getCount]; k++)
    {
        NSLog(@"Added: %@",[AccountStorage getAccountWithIndex:k]);
        [accountPeopleList addObject:[AccountStorage getAccountWithIndex:k]];
    }
    
    ContactStorage * contactsObj = [ContactStorage getSharedInstance];
    NSArray * contactbook = [contactsObj getContacts];
    //add friends into the account
    for(int k = 0; k < [contactbook count]; k++)
    {
        NSLog(@"Added: %@", [contactbook objectAtIndex:k]);
        [accountPeopleList addObject:[contactbook objectAtIndex:k]];
    }
    
    
    
    [transferFromAccount setText:singleAccount.number];
    
    
    
}
-(void) finishWithStatus:(BOOL)status withMessage:(NSString *)message
{
    if(status == YES)
    {
        [self alertStatus:message withTitle:@"Successful"];
        [transferAmount setText:@""];
        [transferFromAccount setText:@""];
        [transferToAccount setText:@""];
    }
    else
    {
        [self alertStatus:message withTitle:@"Failed"];
        
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)pbButton:(id)sender {
    popupMode = contactbookmode;
    
    UIPickerView *myPickerView = [[UIPickerView alloc] initWithFrame:CGRectMake(0, 200, 320, 200)];
    myPickerView.delegate = self;
    myPickerView.showsSelectionIndicator = YES;
    [self.view addSubview:myPickerView];
    
    
    
    
}

- (void)pickerView:(UIPickerView *)pickerView didSelectRow: (NSInteger)row inComponent:(NSInteger)component {
    [pickerView setHidden:YES];
    
    if(popupMode == accountbookmode)
    {
        [transferFromAccount setText: [[AccountStorage getAccountWithIndex:row] getNumber]];
    }
    else
    {
        
        if([Account class] == [[accountPeopleList objectAtIndex:row] class])
        {
            
            [transferToAccount setText:[[accountPeopleList objectAtIndex:row] getNumber]];
        }
        else
            [transferToAccount setText:[[accountPeopleList objectAtIndex:row] email]];
    }
    
}

// tell the picker how many rows are available for a given component
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    if(popupMode == accountbookmode)
    {
        return [AccountStorage getCount];
    }
    
    
    NSLog(@"AccountPeopleList Size%d", [accountPeopleList count]);
    return [accountPeopleList count];
    
}

// tell the picker how many components it will have
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    
    return 1;
    
}

// tell the picker the title for a given component
- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component {
    if(popupMode == accountbookmode)
    {
        return [[AccountStorage getAccountWithIndex:row] getShortName];
    }
    else
    {
        if([Account class] == [[accountPeopleList objectAtIndex:row] class])
        {
            
            return [[accountPeopleList objectAtIndex:row] getShortName];
        }
        else
            return [[accountPeopleList objectAtIndex:row] description];
    }
}

// tell the picker the width of each row for a given component
- (CGFloat)pickerView:(UIPickerView *)pickerView widthForComponent:(NSInteger)component {
    int sectionWidth = 300;
    
    return sectionWidth;
}
- (IBAction)acButton:(id)sender
{
    
    popupMode = accountbookmode;
    
    UIPickerView *myPickerView = [[UIPickerView alloc] initWithFrame:CGRectMake(0, 200, 320, 200)];
    myPickerView.delegate = self;
    myPickerView.showsSelectionIndicator = YES;
    [self.view addSubview:myPickerView];
    
}


- (IBAction)moveMoneyButtonClicked:(id)sender
{
    NSLog(@"Button Clicked");
    
    
    
    MoveMoneyRequest * client = [[MoveMoneyRequest alloc] init];
    client.delegate = self;
    
    NSNumberFormatter *f = [[NSNumberFormatter alloc] init];
    NSNumber * amountNumber = [f numberFromString:transferAmount.text];
    
    switch([self sliderMode: transferModeSlider.value])
    {
        case simple:
            [client sendTransferwithToAccount:transferToAccount.text withFromAccount:transferFromAccount.text withAmount:amountNumber];
            break;
        case send:
            //[self generateQRCodeWithToken:@"TEST TOKEN"];
            [client getTokenWithFromAccount:[transferFromAccount text] withAmount:[transferAmount text]];
            break;
        case receive:
            [self getTokenQR];

            break;
            
    }
    
    
    
    
    
}
-(void) finishWithStatus:(BOOL) status withToken:(NSString*) token
{
    [self generateQRCodeWithToken:token];
}
-(sliderMode) sliderMode: (float) value
{
    if(value < .333)
    {
        return simple;
    }
    else if(value >=.333 && value < .666)
    {
        return receive;
    }
    else{
        return send;
    }
}

-(void) updateSliderWithSender:(UISlider*)slider
{
    switch([self sliderMode: slider.value])
    {
        case simple: [transferModeMessage setText:@"Simple Mode"];
            [transferToAccount setEnabled:YES];
            [transferFromAccount setEnabled:YES];
            [transferAmount setEnabled:YES];
            transferToAccount.backgroundColor = [UIColor whiteColor];
            transferFromAccount.backgroundColor = [UIColor whiteColor];
            transferAmount.backgroundColor = [UIColor whiteColor];
            
            break;
        case receive: [transferModeMessage setText:@"Receive Mode"];
            [transferToAccount setEnabled:YES];
            [transferFromAccount setEnabled:NO];
            [transferAmount setEnabled:NO];
            transferToAccount.backgroundColor = [UIColor whiteColor];
            transferFromAccount.backgroundColor = [UIColor lightGrayColor];
            transferAmount.backgroundColor = [UIColor lightGrayColor];
            break;
        case send: [transferModeMessage setText:@"Send Mode"];
            [transferToAccount setEnabled:NO];
            [transferFromAccount setEnabled:YES];
            [transferAmount setEnabled:YES];
            transferToAccount.backgroundColor = [UIColor lightGrayColor];
            transferFromAccount.backgroundColor = [UIColor whiteColor];
            transferAmount.backgroundColor = [UIColor whiteColor];
            
            break;
    }
    
}
-(void) getTokenQR
{
    ZBarReaderViewController *reader = [ZBarReaderViewController new];
    reader.readerDelegate = self;
    
    [reader.scanner setSymbology: ZBAR_I25
                          config: ZBAR_CFG_ENABLE
                              to: 0];

    
    [self presentModalViewController: reader
                            animated: YES];
    
    

    
}
- (void) imagePickerController: (UIImagePickerController*) reader
 didFinishPickingMediaWithInfo: (NSDictionary*) info
{
    // ADD: get the decode results
    id<NSFastEnumeration> results =
    [info objectForKey: ZBarReaderControllerResults];
    ZBarSymbol *symbol = nil;
    for(symbol in results)
        // EXAMPLE: just grab the first barcode
        break;
    
    // EXAMPLE: do something useful with the barcode data
    NSLog(@"Token Data:%@",symbol.data);
    
    // EXAMPLE: do something useful with the barcode image
   // resultImage.image =
   // [info objectForKey: UIImagePickerControllerOriginalImage];
    
    // ADD: dismiss the controller (NB dismiss from the *reader*!)
    [reader dismissModalViewControllerAnimated: YES];
    
    
    [self sendToken:symbol.data withAccount:[transferToAccount text]];
    
}
- (void) sendToken:(NSString *) token withAccount:(NSString *) account
{
    MoveMoneyRequest * client = [[MoveMoneyRequest alloc] init];
    client.delegate = self;
    [client sendTokenWithToken:token withToAccount:account];
}

- (void) alertStatus:(NSString *)msg withTitle:(NSString *) title
{
    
    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:title message:msg delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
    [alertView show];
}
- (IBAction)sliderMovment:(id)sender {
    UISlider * slider = sender;
    
    [self updateSliderWithSender:slider];
    
}


@end
