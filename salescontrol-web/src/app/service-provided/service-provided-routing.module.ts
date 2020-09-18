import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ServiceProvidedFormComponent } from './service-provided-form/service-provided-form.component';
import { ServiceProvidedListComponent } from './service-provided-list/service-provided-list.component';
import { LayoutComponent } from '../layout/layout.component';
import { AuthGuard } from '../auth.guard';

const routes: Routes = [

  { path : 'service-provided' , component: LayoutComponent, canActivate : [AuthGuard], children: [

    { path : '' , redirectTo : '/service-provided/list' , pathMatch : 'full' },
    { path : 'form' , component: ServiceProvidedFormComponent},
    { path : 'list' , component: ServiceProvidedListComponent}

  ]},

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ServiceProvidedRoutingModule { }
