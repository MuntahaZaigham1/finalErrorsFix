import { Component, OnInit } from "@angular/core";
import { CdkDragDrop, moveItemInArray } from "@angular/cdk/drag-drop";
import { ActivatedRoute, Router } from "@angular/router";
import * as _ from 'lodash';
import { MatDialogRef, MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DashboardService } from "../dashboard.service";
import { IDashboard } from "../idashboard";
import { UpdateDashboardComponent } from "src/app/reporting-module/modalDialogs/update-dashboard/update-dashboard.component";
import { IReport } from 'src/app/reporting-module/pages/myreports/ireport';
import { ConfirmDialogComponent } from 'src/app/common/shared';
import { UsersExtendedService } from 'src/app/extended/admin/user-management/users/index';
import { Observable, ReplaySubject } from 'rxjs';
import { takeUntil, take } from 'rxjs/operators';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: "app-dashboard",
  templateUrl: "./editdashboard.component.html",
  styleUrls: ["./editdashboard.component.scss"]
})
export class EditDashboardComponent implements OnInit {
  confirmDialogRef: MatDialogRef<ConfirmDialogComponent>;
  private destroyed$: ReplaySubject<boolean> = new ReplaySubject(1);
  dashboard: IDashboard;
  selectedDashboardRunningVersion: IDashboard;
  selectedDashboardPublishedVersion: IDashboard;
  selectedVersion: string = "running";
  loading = false;
  dialogRef: MatDialogRef<any>;
  currentElementsPositions = {};
  constructor(
    private route: ActivatedRoute,
    private _snackBar: MatSnackBar,
    private dialog: MatDialog,
    private router: Router,
    private dashboardService: DashboardService,

    public usersExtendedService: UsersExtendedService,
    public translate: TranslateService,
  ) { }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      const id = params.get("id");
      this.dashboardService.getById(id).subscribe(res => {
        this.dashboard = res;
        this.selectedDashboardRunningVersion = res;
      });
    });
  }
  
  switchVersion(event: any){
    let version = event.value;
    if(version == 'published') {
      if(this.selectedDashboardPublishedVersion){
        this.dashboard = this.selectedDashboardPublishedVersion;
      }
      else {
         if(this.dashboard.id){
          this.dashboardService.getPublishedVersion(this.dashboard.id).subscribe((dashboard) => {
            this.selectedDashboardPublishedVersion = dashboard;
            this.dashboard = dashboard;
          });
        }
      }
    }
    else {
      this.dashboard = this.selectedDashboardRunningVersion;
    }
  }

  deleteReport(dashboard_id: any, report_id: any) {
    this.confirmDialogRef = this.dialog.open(ConfirmDialogComponent, {
      disableClose: true,
      data: {
        confirmationType: "delete"
      }
    });
    this.confirmDialogRef.afterClosed().subscribe(action => {
      if (action) {
        this.loading = true;
        this.dashboardService
          .removeReport(dashboard_id, report_id)
          .subscribe(res => {
            if(this.dashboard.reportDetails){
              this.dashboard.reportDetails = this.dashboard.reportDetails.filter((v) => v.id !== report_id);
            }
        	this.loading = false;
            this.showMessage(this.translate.instant('REPORTING.MESSAGES.DASHBOARD.REPORT-REMOVED'));
          });
      }
    });
  }

  updateDashboard() {
    var updated_reports_array = [];
    this.loading = true;
    if(this.dashboard.reportDetails){
      for (var i = 0; i < this.dashboard.reportDetails.length; i++) {
        updated_reports_array.push({
          id: this.dashboard.reportDetails[i].id,
          reportWidth: this.dashboard.reportDetails[i].reportWidth,
        });
      }
     }
    const dashboard = {
      id: this.dashboard.id,
      title: this.dashboard.title,
      description: this.dashboard.description,
		  usersId : this.dashboard.usersId,
      reportDetails: updated_reports_array
    };
    this.dashboardService
      .update(this.dashboard, this.dashboard.id)
      .subscribe(res => {
        this.dashboard = res;
        this.selectedDashboardRunningVersion = res;
        this.selectedDashboardPublishedVersion = {};
        this.loading = false;
        this.showMessage(this.translate.instant('REPORTING.MESSAGES.DASHBOARD.UPDATED'));
      });
  }

  editReport(report: IReport) {
    this.router.navigate([`reporting/reports/${report.id}`]);
  }

  editDashboard(): void {
    const dialogRef = this.dialog.open(UpdateDashboardComponent, {
      panelClass: "fc-modal-dialog",
      data: {
        title: this.dashboard.title,
        description: this.dashboard.description
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      this.dashboard.title = result.dashboardTitle;
      this.dashboard.description = result.dashboarddescription;
      if (result.type != "close") {
        this.updateDashboard();
      }
    });
  }

  previewDashboard(id: any) {
    this.router.navigate([`reporting/dashboard/preview/${id}`]);
  }

  refreshDashboard(id: any) {
    this.confirmDialogRef = this.dialog.open(ConfirmDialogComponent, {
      disableClose: true,
      data: {
        confirmationType: "confirm"
      }
    });
    this.confirmDialogRef.afterClosed().subscribe(action => {
      if (action) {
        this.loading = true;
        this.dashboardService.refresh(id).subscribe(res => {
          this.selectedDashboardRunningVersion = res;
          this.dashboard = res;
          this.selectedVersion = 'running';
          this.loading = false;
          this.showMessage(this.translate.instant('REPORTING.MESSAGES.DASHBOARD.REFRESHED'));
        });
      }
    });
  }

  publishDashboard(id: any) {
    this.confirmDialogRef = this.dialog.open(ConfirmDialogComponent, {
      disableClose: true,
      data: {
        confirmationType: "confirm"
      }
    });
    this.confirmDialogRef.afterClosed().subscribe(action => {
      if (action) {
        this.loading = true;
        this.dashboardService.publish(id).subscribe(res => {
          this.dashboard = res;
          this.selectedDashboardRunningVersion = res;
          this.selectedDashboardPublishedVersion = {};
          this.loading = false;
        });
      }
    });
  }

  deleteDashboard(dashboard: IDashboard) {
    this.confirmDialogRef = this.dialog.open(ConfirmDialogComponent, {
      disableClose: true,
      data: {
        confirmationType: "delete"
      }
    });

    this.confirmDialogRef.afterClosed().pipe(take(1)).subscribe(action => {
      if (action) {
        this.loading = true;
        this.dashboardService.delete(dashboard.id).pipe(take(1)).subscribe(res => {
          this.loading = true;
          this.showMessage(this.translate.instant('REPORTING.MESSAGES.DASHBOARD.DELETED'));
          this.router.navigate(['/reporting/dashboard'])
        });
      }
    });
  }

 refreshChart(report_id:any) {
     if(this.dashboard.reportDetails){
       this.dashboard.reportDetails[this.dashboard.reportDetails.findIndex((x) => x.id == report_id)] = _.clone(
         this.dashboard.reportDetails[this.dashboard.reportDetails.findIndex((x) => x.id == report_id)]
       );
     }
   }

  showMessage(msg: any): void {
    this._snackBar.open(msg, "close", {
      duration: 2000
    });
  }

  drop(event: CdkDragDrop<string[]>) {
    if(this.dashboard.reportDetails){
      moveItemInArray(this.dashboard.reportDetails, event.previousIndex, event.currentIndex);
    }
  }

  setSize(size:any, index:any) {
      if(this.dashboard.reportDetails){
        this.dashboard.reportDetails[index].reportWidth = size;
      }
  }

  ngOnDestroy() {
    this.destroyed$.next(true);
    this.destroyed$.unsubscribe();
  }
}
