import { Component, OnInit, Inject } from '@angular/core';
import { T2ExtendedService } from '../t-2.service';

import { ActivatedRoute,Router } from "@angular/router";
import { FormBuilder } from '@angular/forms';
import { PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';

import { T2NewComponent } from 'src/app/entities/t-2/index';

@Component({
  selector: 'app-t-2-new',
  templateUrl: './t-2-new.component.html',
  styleUrls: ['./t-2-new.component.scss']
})
export class T2NewExtendedComponent extends T2NewComponent implements OnInit {
  
    title:string = "New T2";
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public dialogRef: MatDialogRef<T2NewComponent>,
		@Inject(MAT_DIALOG_DATA) public data: any,
		public pickerDialogService: PickerDialogService,
		public t2ExtendedService: T2ExtendedService,
		public errorService: ErrorService,
		public globalPermissionService: GlobalPermissionService,
	) {
		super(formBuilder, router, route, dialog, dialogRef, data, pickerDialogService, t2ExtendedService, errorService,
		globalPermissionService,
		);
	}
 
	ngOnInit() {
		super.ngOnInit();
  }
     
}
