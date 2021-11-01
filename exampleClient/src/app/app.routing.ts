
import { RouterModule, Routes } from '@angular/router';
import { ModuleWithProviders } from "@angular/core";
import { SwaggerComponent } from './core/swagger/swagger.component';
import { ErrorPageComponent  } from './core/error-page/error-page.component';
import { AuthGuard } from './core/guards/auth.guard';

const routes: Routes = [
	{
		path: '',
		loadChildren: () => import('./extended/core/core.module').then(m => m.CoreExtendedModule),
	},
  	{ path: "swagger-ui", component: SwaggerComponent , canActivate: [ AuthGuard ] },
	{
		path: '',
		loadChildren: () => import('./extended/admin/admin.module').then(m => m.AdminExtendedModule),
	},
	{
		path: '',
		loadChildren: () => import('./extended/account/account.module').then(m => m.AccountExtendedModule),
	},
	{
		path: 'reporting',
		loadChildren: () => import('./reporting-module/reporting.module').then(m => m.ReportingModule),
		canActivate: [AuthGuard]
	},
	{
		path: 't2',
		loadChildren: () => import('./extended/entities/t-2/t-2.module').then(m => m.T2ExtendedModule),
		canActivate: [AuthGuard]
	},
	{
		path: 't1',
		loadChildren: () => import('./extended/entities/t-1/t-1.module').then(m => m.T1ExtendedModule),
		canActivate: [AuthGuard]
	},
	{ path: '**', component:ErrorPageComponent},
	
];

export const routingModule: ModuleWithProviders<any> = RouterModule.forRoot(routes);