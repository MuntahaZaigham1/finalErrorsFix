
import { RouterModule, Routes } from '@angular/router';
import { ModuleWithProviders } from "@angular/core";
import { CanDeactivateGuard } from 'src/app/core/guards/can-deactivate.guard';
import { AuthGuard } from 'src/app/core/guards/auth.guard';
import { T2DetailsExtendedComponent, T2ListExtendedComponent , T2NewExtendedComponent } from './';

const routes: Routes = [
	{ path: '', component: T2ListExtendedComponent, canDeactivate: [CanDeactivateGuard], canActivate: [ AuthGuard ] },
	{ path: ':id', component: T2DetailsExtendedComponent, canDeactivate: [CanDeactivateGuard], canActivate: [ AuthGuard ] },
	{ path: 'new', component: T2NewExtendedComponent, canActivate: [ AuthGuard ] },
];

export const t2Route: ModuleWithProviders<any> = RouterModule.forChild(routes);