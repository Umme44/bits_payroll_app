import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MobileBillDetailComponent } from './mobile-bill-detail.component';

describe('MobileBill Management Detail Component', () => {
  let comp: MobileBillDetailComponent;
  let fixture: ComponentFixture<MobileBillDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MobileBillDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ mobileBill: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MobileBillDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MobileBillDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load mobileBill on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.mobileBill).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
