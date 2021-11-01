import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { FormArray, FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';

import { T1Service } from '../t-1.service';
import { IT1
 } from '../it-1';
import { BaseDetailsComponent, FieldType, PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';


@Component({
  selector: 'app-t-1-details',
  templateUrl: './t-1-details.component.html',
  styleUrls: ['./t-1-details.component.scss']
})
export class T1DetailsComponent extends BaseDetailsComponent<IT1> implements OnInit {
	title = 'T1';
	parentUrl = 't1';
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public t1Service: T1Service,
		public pickerDialogService: PickerDialogService,
		public errorService: ErrorService,
		public globalPermissionService: GlobalPermissionService,
	) {
		super(formBuilder, router, route, dialog, pickerDialogService, t1Service, errorService);
  }

	ngOnInit() {
		this.entityName = 'T1';
		this.setAssociations();
		super.ngOnInit();
		this.setForm();
    	this.getItem();
	}
  
  setForm(){
    this.itemForm = this.formBuilder.group({
      id: [{value: '', disabled: true}, Validators.required],
      
    });
    
    this.fields = [
      ];
      
  }
  
  onItemFetched(item: IT1) {
    this.item = item;

     this.itemForm.patchValue(item);

  }
  
  setAssociations(){
    this.associations = [
		];
		
		this.childAssociations = this.associations.filter(association => {
			return (association.isParent);
		});

		this.parentAssociations = this.associations.filter(association => {
			return (!association.isParent);
		});
	}
	
	onSubmit() {
    		let t1 = this.itemForm.getRawValue();



                super.onSubmit(t1);



    	}
}
