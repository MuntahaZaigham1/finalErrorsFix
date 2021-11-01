import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { Router, ActivatedRoute } from '@angular/router';

import { T2ExtendedService } from '../t-2.service';
import { T2NewExtendedComponent } from '../new/t-2-new.component';
import { PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { Globals } from 'src/app/core/services/globals';

import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';
import { T2ListComponent } from 'src/app/entities/t-2/index';

@Component({
  selector: 'app-t-2-list',
  templateUrl: './t-2-list.component.html',
  styleUrls: ['./t-2-list.component.scss']
})
export class T2ListExtendedComponent extends T2ListComponent implements OnInit {

	title:string = "T2";
	constructor(
		public router: Router,
		public route: ActivatedRoute,
		public global: Globals,
		public dialog: MatDialog,
		public changeDetectorRefs: ChangeDetectorRef,
		public pickerDialogService: PickerDialogService,
		public t2Service: T2ExtendedService,
		public errorService: ErrorService,
		public globalPermissionService: GlobalPermissionService,
	) { 
		super(router, route, global, dialog, changeDetectorRefs, pickerDialogService, t2Service, errorService,
		globalPermissionService,
		)
  }

	ngOnInit() {
		super.ngOnInit();
	}
  
	addNew() {
		super.addNew(T2NewExtendedComponent);
	}
  
}
