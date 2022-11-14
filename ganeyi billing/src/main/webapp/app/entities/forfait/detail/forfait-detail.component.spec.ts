import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ForfaitDetailComponent } from './forfait-detail.component';

describe('Forfait Management Detail Component', () => {
  let comp: ForfaitDetailComponent;
  let fixture: ComponentFixture<ForfaitDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ForfaitDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ forfait: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(ForfaitDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ForfaitDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load forfait on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.forfait).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
