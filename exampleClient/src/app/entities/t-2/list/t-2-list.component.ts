import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { IT2 } from '../it-2';
import { T2Service } from '../t-2.service';
import { Router, ActivatedRoute } from '@angular/router';
import { T2NewComponent } from '../new/t-2-new.component';
import { BaseListComponent, ListColumnType, PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { Globals } from 'src/app/core/services/globals';
import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';


@Component({
  selector: 'app-t-2-list',
  templateUrl: './t-2-list.component.html',
  styleUrls: ['./t-2-list.component.scss']
})
export class T2ListComponent extends BaseListComponent<IT2> implements OnInit {

	title = 'T2';
	constructor(
		public router: Router,
		public route: ActivatedRoute,
		public global: Globals,
		public dialog: MatDialog,
		public changeDetectorRefs: ChangeDetectorRef,
		public pickerDialogService: PickerDialogService,
		public t2Service: T2Service,
		public errorService: ErrorService,
		public globalPermissionService: GlobalPermissionService,
	) { 
		super(router, route, dialog, global, changeDetectorRefs, pickerDialogService, t2Service, errorService)
  }

	ngOnInit() {
		this.entityName = 'T2';
		this.setAssociation();
		this.setColumns();
		this.primaryKeys = ['id', 'pic', ]
		super.ngOnInit();
	}
  
  
	setAssociation(){
  	
		this.associations = [
		];
	}
  
  	setColumns(){
  		this.columns = [
    		{
				column: 'id',
				searchColumn: 'id',
				label: 'id',
				sort: true,
				filter: true,
				type: ListColumnType.Number
			},
    		{
				column: 'pic',
				searchColumn: 'pic',
				label: 'pic',
				sort: true,
				filter: true,
				type: ListColumnType.String
			},
		  	{
				column: 'actions',
				label: 'Actions',
				sort: false,
				filter: false,
				type: ListColumnType.String
			}
		];
		this.selectedColumns = this.columns;
		this.displayedColumns = this.columns.map((obj) => { return obj.column });
  	}
  addNew(comp: any) {
	if(!comp){
		comp = T2NewComponent;
	}
	super.addNew(comp);
  }
  
}
