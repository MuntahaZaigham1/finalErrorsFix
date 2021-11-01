
import { RouterModule, Routes } from '@angular/router';
import { ModuleWithProviders } from "@angular/core";
// import { CanDeactivateGuard } from 'src/app/core/guards/can-deactivate.guard';
// import { AuthGuard } from 'src/app/core/guards/auth.guard';

// import { T2DetailsComponent, T2ListComponent, T2NewComponent } from './';

const routes: Routes = [
	// { path: '', component: T2ListComponent, canDeactivate: [CanDeactivateGuard], canActivate: [ AuthGuard ] },
	// { path: ':id', component: T2DetailsComponent, canDeactivate: [CanDeactivateGuard], canActivate: [ AuthGuard ] },
	// { path: 'new', component: T2NewComponent, canActivate: [ AuthGuard ] },
];

export const t2Route: ModuleWithProviders<any> = RouterModule.forChild(routes);