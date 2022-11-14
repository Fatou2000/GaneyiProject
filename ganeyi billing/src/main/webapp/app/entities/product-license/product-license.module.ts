import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProductLicenseComponent } from './list/product-license.component';
import { ProductLicenseDetailComponent } from './detail/product-license-detail.component';
import { ProductLicenseUpdateComponent } from './update/product-license-update.component';
import { ProductLicenseDeleteDialogComponent } from './delete/product-license-delete-dialog.component';
import { ProductLicenseRoutingModule } from './route/product-license-routing.module';

@NgModule({
  imports: [SharedModule, ProductLicenseRoutingModule],
  declarations: [
    ProductLicenseComponent,
    ProductLicenseDetailComponent,
    ProductLicenseUpdateComponent,
    ProductLicenseDeleteDialogComponent,
  ],
})
export class ProductLicenseModule {}
