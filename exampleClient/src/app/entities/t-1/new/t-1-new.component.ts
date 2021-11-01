import { Component, OnInit, Inject } from '@angular/core';
import { ActivatedRoute,Router} from "@angular/router";
import { FormArray, FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { T1Service } from '../t-1.service';
 import { IT1
 } from '../it-1';
import { BaseNewComponent, FieldType, PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';


@Component({
  selector: 'app-t-1-new',
  templateUrl: './t-1-new.component.html',
  styleUrls: ['./t-1-new.component.scss']
})
export class T1NewComponent extends BaseNewComponent<IT1> implements OnInit {
  
    title:string = "New T1";
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public dialogRef: MatDialogRef<T1NewComponent>,
		@Inject(MAT_DIALOG_DATA) public data: any,
		public pickerDialogService: PickerDialogService,
		public t1Service: T1Service,
		public errorService: ErrorService,
		public globalPermissionService: GlobalPermissionService,
	) {
		super(formBuilder, router, route, dialog, dialogRef, data, pickerDialogService, t1Service, errorService);
	}
 
		ngOnInit() {
		this.entityName = 'T1';
		this.setAssociations();
		super.ngOnInit();
    	this.setForm();
		this.checkPassedData();
        }


	setForm(){
 		this.itemForm = this.formBuilder.group({
    });
    
    this.fields = [
		];
	}
	 
	setAssociations(){
  	
		this.associations = [
		];
		this.parentAssociations = this.associations.filter(association => {
			return (!association.isParent);
		});

	}
	
	onSubmit() {
		let t1 = this.itemForm.getRawValue();



            super.onSubmit(t1);



	}
    
}
