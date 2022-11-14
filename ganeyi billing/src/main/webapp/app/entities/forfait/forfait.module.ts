import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ForfaitComponent } from './list/forfait.component';
import { ForfaitDetailComponent } from './detail/forfait-detail.component';
import { ForfaitUpdateComponent } from './update/forfait-update.component';
import { ForfaitDeleteDialogComponent } from './delete/forfait-delete-dialog.component';
import { ForfaitRoutingModule } from './route/forfait-routing.module';

@NgModule({
  imports: [SharedModule, ForfaitRoutingModule],
  declarations: [ForfaitComponent, ForfaitDetailComponent, ForfaitUpdateComponent, ForfaitDeleteDialogComponent],
})
export class ForfaitModule {}
