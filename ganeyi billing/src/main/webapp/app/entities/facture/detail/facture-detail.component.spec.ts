import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FactureDetailComponent } from './facture-detail.component';

describe('Facture Management Detail Component', () => {
  let comp: FactureDetailComponent;
  let fixture: ComponentFixture<FactureDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FactureDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ facture: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(FactureDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FactureDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load facture on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.facture).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
