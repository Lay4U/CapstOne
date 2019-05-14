import slate


def extract_text_from_pdf(pdf_path):
    with open(pdf_path) as fh:
        document = slate.PDF(fh, password='', just_text=1)

    for page in document:
        print(page)

print(extract_text('neural_architecture_search.pdf'))
