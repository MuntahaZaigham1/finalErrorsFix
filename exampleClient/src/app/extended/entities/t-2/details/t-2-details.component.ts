import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';

import { T2ExtendedService } from '../t-2.service';

import { PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';


import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';
import { T2DetailsComponent } from 'src/app/entities/t-2/index';

@Component({
  selector: 'app-t-2-details',
  templateUrl: './t-2-details.component.html',
  styleUrls: ['./t-2-details.component.scss']
})
export class T2DetailsExtendedComponent extends T2DetailsComponent implements OnInit {
	title:string='T2';
	parentUrl:string='t2';
	//roles: IRole[];  
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public t2ExtendedService: T2ExtendedService,
		public pickerDialogService: PickerDialogService,
		public errorService: ErrorService,
		public globalPermissionService: GlobalPermissionService,
	) {
		super(formBuilder, router, route, dialog, t2ExtendedService, pickerDialogService, errorService,
		globalPermissionService,
		);
  }

	ngOnInit() {
		super.ngOnInit();
  }
  
}
