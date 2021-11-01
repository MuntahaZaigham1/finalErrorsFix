import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';

import { T1ExtendedService } from '../t-1.service';

import { PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';


import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';
import { T1DetailsComponent } from 'src/app/entities/t-1/index';

@Component({
  selector: 'app-t-1-details',
  templateUrl: './t-1-details.component.html',
  styleUrls: ['./t-1-details.component.scss']
})
export class T1DetailsExtendedComponent extends T1DetailsComponent implements OnInit {
	title:string='T1';
	parentUrl:string='t1';
	//roles: IRole[];  
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public t1ExtendedService: T1ExtendedService,
		public pickerDialogService: PickerDialogService,
		public errorService: ErrorService,
		public globalPermissionService: GlobalPermissionService,
	) {
		super(formBuilder, router, route, dialog, t1ExtendedService, pickerDialogService, errorService,
		globalPermissionService,
		);
  }

	ngOnInit() {
		super.ngOnInit();
  }
  
}
