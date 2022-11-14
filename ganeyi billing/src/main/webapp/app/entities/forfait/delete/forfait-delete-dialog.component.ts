import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IForfait } from '../forfait.model';
import { ForfaitService } from '../service/forfait.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './forfait-delete-dialog.component.html',
})
export class ForfaitDeleteDialogComponent {
  forfait?: IForfait;

  constructor(protected forfaitService: ForfaitService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.forfaitService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
