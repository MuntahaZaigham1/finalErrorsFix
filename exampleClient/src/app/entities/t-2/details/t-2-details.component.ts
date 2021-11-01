import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { FormArray, FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';

import { T2Service } from '../t-2.service';
import { IT2
 } from '../it-2';
import { BaseDetailsComponent, FieldType, PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';


@Component({
  selector: 'app-t-2-details',
  templateUrl: './t-2-details.component.html',
  styleUrls: ['./t-2-details.component.scss']
})
export class T2DetailsComponent extends BaseDetailsComponent<IT2> implements OnInit {
	title = 'T2';
	parentUrl = 't2';
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public t2Service: T2Service,
		public pickerDialogService: PickerDialogService,
		public errorService: ErrorService,
		public globalPermissionService: GlobalPermissionService,
	) {
		super(formBuilder, router, route, dialog, pickerDialogService, t2Service, errorService);
  }

	ngOnInit() {
		this.entityName = 'T2';
		this.setAssociations();
		super.ngOnInit();
		this.setForm();
    	this.getItem();
	}
  
  setForm(){
    this.itemForm = this.formBuilder.group({
      id: [{value: '', disabled: true}, Validators.required],
      pic: ['', Validators.required],
      
    });
    
    this.fields = [
        {
		  name: 'pic',
		  label: 'pic',
		  isRequired: true,
		  isAutoGenerated: false,
	      type: FieldType.String,
	    },
      ];
      
  }
  
  onItemFetched(item: IT2) {
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
    		let t2 = this.itemForm.getRawValue();



                super.onSubmit(t2);



    	}
}
