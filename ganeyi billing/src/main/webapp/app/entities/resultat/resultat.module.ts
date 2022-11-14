import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ResultatComponent } from './list/resultat.component';
import { ResultatDetailComponent } from './detail/resultat-detail.component';
import { ResultatUpdateComponent } from './update/resultat-update.component';
import { ResultatDeleteDialogComponent } from './delete/resultat-delete-dialog.component';
import { ResultatRoutingModule } from './route/resultat-routing.module';

@NgModule({
  imports: [SharedModule, ResultatRoutingModule],
  declarations: [ResultatComponent, ResultatDetailComponent, ResultatUpdateComponent, ResultatDeleteDialogComponent],
})
export class ResultatModule {}
