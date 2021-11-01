import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { FormArray, FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';

import { PermissionService } from '../permission.service';
import { IPermission
 } from '../ipermission';
import { BaseDetailsComponent, FieldType, PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';


@Component({
  selector: 'app-permission-details',
  templateUrl: './permission-details.component.html',
  styleUrls: ['./permission-details.component.scss']
})
export class PermissionDetailsComponent extends BaseDetailsComponent<IPermission> implements OnInit {
	title = 'Permission';
	parentUrl = 'permission';
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public permissionService: PermissionService,
		public pickerDialogService: PickerDialogService,
		public errorService: ErrorService,
		public globalPermissionService: GlobalPermissionService,
	) {
		super(formBuilder, router, route, dialog, pickerDialogService, permissionService, errorService);
  }

	ngOnInit() {
		this.entityName = 'Permission';
		this.setAssociations();
		super.ngOnInit();
		this.setForm();
    	this.getItem();
	}
  
  setForm(){
    this.itemForm = this.formBuilder.group({
      displayName: ['', Validators.required],
      id: [{value: '', disabled: true}, Validators.required],
      name: ['', Validators.required],
      
    });
    
    this.fields = [
        {
		  name: 'displayName',
		  label: 'display Name',
		  isRequired: true,
		  isAutoGenerated: false,
	      type: FieldType.String,
	    },
        {
		  name: 'id',
		  label: 'id',
		  isRequired: true,
		  isAutoGenerated: true,
	      type: FieldType.Number,
	    },
        {
		  name: 'name',
		  label: 'name',
		  isRequired: true,
		  isAutoGenerated: false,
	      type: FieldType.String,
	    },
      ];
      
  }
  
  onItemFetched(item: IPermission) {
    this.item = item;



     this.itemForm.patchValue(item);

  }
  
  setAssociations(){
    this.associations = [
      {
        column: [
		],
		isParent: true,
		table: 'rolepermission',
		type: 'OneToMany',
		label: 'rolepermissions',
		},
		];
		
		this.childAssociations = this.associations.filter(association => {
			return (association.isParent);
		});

		this.parentAssociations = this.associations.filter(association => {
			return (!association.isParent);
		});
	}
	
	onSubmit() {
    		let permission = this.itemForm.getRawValue();



                super.onSubmit(permission);



    	}
}
