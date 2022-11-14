import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProductLicense } from '../product-license.model';
import { ProductLicenseService } from '../service/product-license.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './product-license-delete-dialog.component.html',
})
export class ProductLicenseDeleteDialogComponent {
  productLicense?: IProductLicense;

  constructor(protected productLicenseService: ProductLicenseService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.productLicenseService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
