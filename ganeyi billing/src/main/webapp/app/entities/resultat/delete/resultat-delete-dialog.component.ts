import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IResultat } from '../resultat.model';
import { ResultatService } from '../service/resultat.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './resultat-delete-dialog.component.html',
})
export class ResultatDeleteDialogComponent {
  resultat?: IResultat;

  constructor(protected resultatService: ResultatService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.resultatService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
