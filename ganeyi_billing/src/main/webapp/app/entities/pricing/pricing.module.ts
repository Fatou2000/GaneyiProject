import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PricingComponent } from './list/pricing.component';
import { PricingDetailComponent } from './detail/pricing-detail.component';
import { PricingUpdateComponent } from './update/pricing-update.component';
import { PricingDeleteDialogComponent } from './delete/pricing-delete-dialog.component';
import { PricingRoutingModule } from './route/pricing-routing.module';

@NgModule({
  imports: [SharedModule, PricingRoutingModule],
  declarations: [PricingComponent, PricingDetailComponent, PricingUpdateComponent, PricingDeleteDialogComponent],
})
export class PricingModule {}
