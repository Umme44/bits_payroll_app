import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PfCollectionDetailComponent } from './pf-collection-detail.component';

describe('PfCollection Management Detail Component', () => {
  let comp: PfCollectionDetailComponent;
  let fixture: ComponentFixture<PfCollectionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PfCollectionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ pfCollection: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PfCollectionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PfCollectionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pfCollection on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.pfCollection).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
